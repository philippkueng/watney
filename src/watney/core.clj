(ns watney.core
  (:require [net.cgrand.enlive-html :as html]))

(defn ^:private parse
  "Parse the given HTML using Enlive and return the tree"
  [html-string]
  (-> html-string
      java.io.StringReader.
      html/html-resource))

(defn convert
  "Convert the HTML string given into Markdown"
  [html-string]
  html-string)

