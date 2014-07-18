(defproject jetpack "0.1.0-SNAPSHOT"
  :description "A Clojure web site bootstrapping library."
  :url "http://github.com/rosejn/jetpack"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [jarohen/nomad "0.6.3"]              ; environment & config files
                 [com.taoensso/timbre "3.2.0"]        ; logging
                 [com.stuartsierra/component "0.2.1"] ; managing stateful components
                 [org.clojure/tools.namespace "0.2.5"] ; ns helpers
                 [org.clojure/tools.trace "0.7.8"]    ; quick debug tool
                 [clj-time "0.6.0"]                   ; time & date utils
                 [me.raynes/fs "1.4.5"]               ; file system utils

                 [http-kit "2.0.0"]
                 [ring "1.3.0"]
                 ;[ring/ring-devel "1.1.8"]
                 [ring/ring-json "0.2.0"]
                 [ring-cors "0.1.0"]
                 [ring-basic-authentication "1.0.2"]
                 [compojure "1.1.6"]
                 [com.cemerick/friend "0.2.1"]

                 [hiccup "1.0.4"]
                 [markdown-clj "0.9.33"]
                 [enlive "1.1.4"]

                 [jarohen/chord "0.4.2"]  ; core.async channels over http
                 ;[org.clojure/core.async "0.1.256.0-1bf8cf-alpha"]
                 ;[clj-jgit "0.6.1"]
                 ]
  :main jetpack.core

  :profiles
  {:dev  {:dependencies  [[javax.servlet/servlet-api "2.5"]
                          [ring-mock "0.1.5"]]
          :source-paths  ["dev"]
          }
   }

  :resources-path "public"

  :plugins [[lein-daemon "0.5.4"]
            [lein-ring "0.8.8"]]

  :daemon {:site {:ns jetpack.main
                  :pidfile "jetpack-daemon.pid"}}

  :ring {:handler jetpack.core/app
         :auto-reload? true}

  :repl-options {:init-ns user}

  :min-lein-version "2.0.0"

  :jvm-opts  ["-Xmx400m"
             "-XX:+UseConcMarkSweepGC"]
  )
