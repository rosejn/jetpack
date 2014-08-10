(ns jetpack.components.router
  (:require [com.stuartsierra.component :as component]
            [jetpack.core :refer [app-handler]]
            [taoensso.timbre :as log]))

(defrecord Router [page-loader]
  component/Lifecycle
  (start [component]
    (if (:handler component)
      component
      (do
        (log/info "Starting router")
        (assoc component :handler (app-handler page-loader)))))

  (stop [component]
    (log/info "Stopping router")
    (dissoc component :handler)))

(defn router-component [config]
  (log/info "router config:")
  (log/info config)
  (map->Router config))

