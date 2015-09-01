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

(defn ^:private convert-entity
  "Returns Markdown when given an Enlive data tree."
  [html-tree]
  (->> html-tree
       (map (fn [node]
              (case (:tag node)
                :h1 (str "# " (first (:content node)))
                :h2 (str "## " (first (:content node)))
                :h3 (str "### " (first (:content node)))
                :h4 (str "#### " (first (:content node)))

                ;; if any of the children of this :li is a :ul, then return 2 spaces here
                :li (if (contains-node? (:content node) :ul)
                      (str "  " (convert-entity (:content node)))
                      (str "* " (first (:content node))))
                (convert-entity (:content node)))))
       (remove empty?)
       (str/join "\n")))

(defn convert
  "Convert the HTML string given into Markdown"
  [html-string]
  (-> html-string
      parse
      convert-entity
      (str "\n")))


