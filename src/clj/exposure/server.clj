(ns exposure.server
  (:require [exposure.handler :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]
            [exposure.config :refer [config]])
  (:gen-class))

 (defn -main [& args]
   (run-jetty app {:port (config :port 3000)
                   :join? false}))
