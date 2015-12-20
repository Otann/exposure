(ns exposure.handler
  (:require [environ.core :refer [env]]
            [compojure.core :refer [GET defroutes context]]
            [compojure.route :refer [not-found resources]]

            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]

            [exposure.layout :as layout]
            [exposure.instagram-api :as instagram]))


(defroutes routes
  (GET "/"      [] layout/loading-page)
  (GET "/auth"  [] layout/auth-page)

  (context "/api" []
    (GET "/ping" [] {:status 200 :body "pong"})
    (GET "/redirected" req (instagram/handle-redirect req)))

  ; catch-all handler to allow client-side routing
  (GET "/*" [] layout/loading-page))

(def app
  ;; #'routes expands to (var routes) so that when we reload our code,
  ;; the server is forced to re-resolve the symbol in the var
  ;; rather than having its own copy. When the root binding
  ;; changes, the server picks it up without having to restart.
  (let [handler (-> #'routes
                    (wrap-json-response)
                    (wrap-defaults site-defaults))]
    (if (env :dev)
      (-> handler
          wrap-exceptions
          wrap-reload)
      handler)))
