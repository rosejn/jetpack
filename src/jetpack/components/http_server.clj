(ns jetpack.components.http-server
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :as httpkit]
            [taoensso.timbre :as log])
  (:use clojure.tools.trace))

(defrecord HTTPServer [port router]
  component/Lifecycle
  (start [component]
    (log/info "Listening on port " port)
    (if-not (:stop component)
      (assoc component :stop (httpkit/run-server (:handler router) {:port port}))
      component))

  (stop [component]
    (when-let [stop (:stop component)]
      (log/info "stopping http server")
      (stop))
    (dissoc component :stop)))

(defn http-server-component
  [config]
  (log/info "http config:")
  (log/info config)
  (map->HTTPServer config))
