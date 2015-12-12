(defproject jetpack "0.1.0-SNAPSHOT"
  :description "A Clojure web site bootstrapping library."
  :url "http://github.com/rosejn/jetpack"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0-beta2"]
                 [jarohen/nomad "0.7.2"]              ; environment & config files
                 [com.taoensso/timbre "4.1.4"]        ; logging
                 [com.stuartsierra/component "0.3.1"] ; managing stateful components
                 [org.clojure/tools.namespace "0.2.10"] ; ns helpers
                 [org.clojure/tools.trace "0.7.9"]    ; quick debug tool
                 [clj-time "0.11.0"]                   ; time & date utils
                 [me.raynes/fs "1.4.6"]               ; file system utils
                 [clojure-watch "0.1.11"]             ; watch fs for updates

                 [http-kit "2.1.19"]
                 [ring "1.4.0"]
                 ;[ring/ring-devel "1.1.8"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.7"]
                 [ring-basic-authentication "1.0.5"]
                 [compojure "1.4.0"]
                 [com.cemerick/friend "0.2.1"]

                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.82"]
                 [enlive "1.1.6"]

                 [jarohen/chord "0.7.0"]  ; core.async channels over http
                 ]
  ;:main jetpack.core

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
