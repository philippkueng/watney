(ns watney.core
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str]))

(defn ^:private parse
  "Parse the given HTML using Enlive and return the tree"
  [html-string]
  (-> html-string
      java.io.StringReader.
      html/html-resource))

(defn ^:private contains-node?
  "Given an enlive tree, tells if a given element is contained in the first level"
  [html-tree element]
  (some #(= element (:tag %)) html-tree))

(defn ^:private whitespace-str
  "Generates a string of spaces of a given length"
  [length]
  (->> (range length)
       (map (fn [i] " "))
       (apply str)))

(defn ^:private list-prefix-character
  "Returnes `1.  ` or `*  ` depending on what kind of list it is"
  [options]
  (if (= :ol (:list-type options))
    "1. "
    "* "))

(defn ^:private convert-entity
  "Returns Markdown when given an Enlive data tree."
  [html-tree options]
  (let [convert-content #(->> (:content %)
                              (map (fn [sub-node]
                                     (if (= (type sub-node) java.lang.String)
                                       sub-node
                                       (->> (convert-entity (list sub-node) options)))))
                              (clojure.string/join ""))]
    (str (whitespace-str (:prefix-spaces options))
         (->> html-tree
              (map (fn [node]
                     (case (:tag node)
                       :h1 (str "\n# " (convert-content node))
                       :h2 (str "\n## " (convert-content node))
                       :h3 (str "\n### " (convert-content node))
                       :h4 (str "\n#### " (convert-content node))
                       :p  (str "\n" (convert-content node))
                       :a  (str "[" (first (:content node)) "](" (:href (:attrs node)) ")")
                       :img (str "![" (:alt (:attrs node)) "](" (:src (:attrs node)) ")")
                       :code (if (:code-block options)
                               (str "```"
                                    (:class (:attrs node))
                                    "\n"
                                    (convert-content node) "```")
                               (str "`" (convert-content node) "`"))
                       :pre (str "\n" (convert-entity (:content node) (assoc options
                                                                             :code-block true)))

                       :ul (str "\n" (convert-entity (:content node) {:prefix-spaces 0
                                                                      :list-type :ul}))
                       :ol (str "\n" (convert-entity (:content node) {:prefix-spaces 0
                                                                      :list-type :ol}))

                       ;; if any of the children of this :li is a :ul, then return 2 spaces here
                       :li (if (or (contains-node? (:content node) :ol)
                                   (contains-node? (:content node) :ul))
                             (str (if (= (type (first (:content node))) java.lang.String)
                                    (str (list-prefix-character options)
                                         (first (:content node))
                                         "\n")
                                    nil)
                                  (->> (:content node)
                                       (filter #(or (= (:tag %) :ul)
                                                    (= (:tag %) :ol)))
                                       first
                                       :content
                                       (map (fn [entity]
                                              (convert-entity (list entity) (assoc options
                                                                                   :prefix-spaces (+ (:prefix-spaces options) 2)))))
                                       (str/join "\n")))
                             (str (list-prefix-character options)
                                  (first (:content node))))
                       (convert-entity (:content node) {:prefix-spaces 0}))))
              (remove empty?)
              (str/join "\n")))))

(defn ^:private without-first-character
  "Returns the content string without the first character"
  [content]
  (->> content
       rest
       (apply str)))

(defn convert
  "Convert the HTML string given into Markdown"
  [html-string]
  (-> html-string
      parse
      (convert-entity {:prefix-spaces 0})
      (str "\n")
      
      ;; remove the first newline character (top of the file) introduced when converting the html
      without-first-character))



 #_(def tables (-> the-page
                    java.io.StringReader.
                    html/html-resource
                    (html/select [:table :table])))
