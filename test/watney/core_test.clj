(ns watney.core-test
  (:require [watney.core :as w]
            [markdown.core :as md])
  (:use [midje.sweet]))

(fact "parsing the simple.html file should return a correct simple.md"
  (w/convert (slurp "fixtures/simple.html")) => (slurp "fixtures/simple.md"))
