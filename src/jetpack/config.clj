(ns jetpack.config
  (:require
    [taoensso.timbre :as timbre]
    [clojure.java.io :as io]
    [nomad :refer [defconfig]]))

(timbre/set-config! [:appenders :spit :enabled?] true)
(timbre/set-config! [:shared-appender-config :spit-filename] "log/site.log")

(defconfig CONFIG (io/resource "config.edn"))

