(ns user
  (:require [clojure.test :refer [run-tests]]
            [clojure.string :as string]
            [clojure.repl :refer [doc find-doc]]
            [clojure.reflect :refer (reflect)]
            [clojure.pprint :refer :all]
            [clojure.tools.namespace.repl :refer [refresh]]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :as log]
            [clojure.java.io :as io]
            [nomad :refer [defconfig]]
            [jetpack.system :refer [jetpack-system]]
            ;[user.system :refer [system]]
            jetpack.core))

(def system nil)

(defconfig CONFIG (io/resource "config.edn"))

(defn init []
  (alter-var-root #'system
    (constantly (jetpack-system (CONFIG)))))

(defn start
  "Starts the system if it hasn't already been started."
  []
  (log/info "starting system")
  (alter-var-root #'system component/start)
  (log/info "system started")
  :started)

(defn stop
  "Stops the system if it is running."
  []
  (log/info "stopping system")
  (alter-var-root #'system #(when % (component/stop %)))
  (log/info "system stopped")
  :stopped)

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'user/go))

