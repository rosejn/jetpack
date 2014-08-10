(ns jetpack.system
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as log]
            [jetpack.components.page-loader :refer [page-loader-component]]
            [jetpack.components.router :refer [router-component]]
            [jetpack.components.http-server :refer [http-server-component]]))

(defn jetpack-system
  [{:keys [loader router http] :as config}]
  (log/info "Starting jetpack system...")
  (component/system-map
    :page-loader (page-loader-component loader)
    :router  (component/using (router-component router) [:page-loader])
    :server  (component/using (http-server-component http) [:router])))

