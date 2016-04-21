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

(defn ^:private convert-content
  "Return the converted contents of an element."
  [contents prefix-spaces]
  )

(defn ^:private convert-entity
  "Returns Markdown when given an Enlive data tree."
  [html-tree prefix-spaces]
  (let [convert-content #(->> (:content %)
                              (map (fn [sub-node]
                                     (if (= (type sub-node) java.lang.String)
                                       sub-node
                                       (->> (convert-entity (list sub-node) prefix-spaces)))))
                              (clojure.string/join ""))]
    (str (whitespace-str prefix-spaces)
         (->> html-tree
              (map (fn [node]
                     (case (:tag node)
                       :h1 (str "# " (convert-content node))
                       :h2 (str "## " (convert-content node))
                       :h3 (str "### " (convert-content node))
                       :h4 (str "#### " (convert-content node))
                       :p  (str "\n" (convert-content node))
                       :a  (str "[" (first (:content node)) "](" (:href (:attrs node)) ")")
                       :img (str "![" (:alt (:attrs node)) "](" (:src (:attrs node)) ")")

                       ;; if any of the children of this :li is a :ul, then return 2 spaces here
                       :li (if (contains-node? (:content node) :ul)
                             (str (if (= (type (first (:content node))) java.lang.String)
                                    (str "*  " (first (:content node)) "\n")
                                    nil)
                                  (->> (:content node)
                                       (filter #(= (:tag %) :ul))
                                       first
                                       :content
                                       (map (fn [entity]
                                              (convert-entity (list entity) (+ prefix-spaces 2))))
                                       (str/join "\n")))
                             (str "*  " (first (:content node))))
                       (convert-entity (:content node) 0))))
              (remove empty?)
              (str/join "\n")))))

(defn convert
  "Convert the HTML string given into Markdown"
  [html-string]
  (-> html-string
      parse
      (convert-entity 0)
      (str "\n")))




 #_(def tables (-> the-page
                    java.io.StringReader.
                    html/html-resource
                    (html/select [:table :table])))
