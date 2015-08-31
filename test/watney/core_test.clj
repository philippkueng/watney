(ns watney.core-test
  (:require [watney.core :as w]
            [markdown.core :as md])
  (:use [midje.sweet]))

(fact "parsing the simple.html file should return a correct simple.md"
  (let [markdown (slurp "fixtures/simple.md")]
    (w/convert (md/md-to-html-string markdown)) => markdown))
