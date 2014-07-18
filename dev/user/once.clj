(ns user.once
  "This namespace will not reload when doing a refresh."
  (:require [clojure.tools.namespace.repl :refer [disable-reload!]]))

(disable-reload!)

(defonce system nil)
