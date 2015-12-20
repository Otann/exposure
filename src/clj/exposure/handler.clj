(ns exposure.handler
  (:require [environ.core :refer [env]]
            [compojure.core :refer [GET defroutes context]]
            [compojure.route :refer [not-found resources]]

            [ring.util.response :refer [redirect]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]
            [prone.middleware :refer [wrap-exceptions]]

            [exposure.constants :as constants]
            [exposure.layout :as layout]
            [exposure.instagram-api :as instagram]))


(defroutes routes
  (GET "/"      [] layout/reagent-page)
  (GET "/auth"  [] layout/auth-page)

  (context "/api" []
    (GET "/ping" [] {:status 200 :body "pong"})
    (GET "/auth" [] (redirect (instagram/auth-url)))
    (GET "/redirected" req (if-let [data (instagram/redirect-data req)]
                             (layout/persist-and-redirect
                               {:url "/"
                                :key constants/localstorage-profile-key
                                :data data})
                             {:status 500
                              :body "Authorization error"}))
    (GET "/search" [token tag] (instagram/search token tag)))

  ; catch-all handler to allow client-side routing
  (GET "/*" [] layout/reagent-page))

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
