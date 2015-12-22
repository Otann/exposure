(defproject exposure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.0"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170" :scope "provided"]
                 [com.taoensso/timbre "4.1.4"]              ; Clojure/Script logging

                 [ring "1.4.0"]
                 [ring-server "0.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-json "0.4.0"]

                 [compojure "1.4.0"]                        ; backend routes
                 [prone "0.8.2"]                            ; exceptions middleware
                 [hiccup "1.0.5"]                           ; html generation
                 [environ "1.0.1"]                          ; config from env
                 [cheshire "5.5.0"]                         ; json parsing/generation
                 [clj-http "2.0.0"]                         ; http client for backend

                 ;; Frontend
                 [reagent "0.5.1"]                          ; React rendering wrapper
                 [re-frame "0.6.0"]                         ; data-flow library
                 [bidi "1.20.3"]                            ; frontend routing
                 [kibu/pushy "0.3.2"]                       ; HTML5 history
                 [cljs-http "0.1.39"]                       ; http library
                 ]

  :plugins [[lein-environ "1.0.1"]
            [lein-cljsbuild "1.1.1"]
            [lein-asset-minifier "0.2.2"
             :exclusions [org.clojure/clojure]]]

  :main exposure.server
  :uberjar-name "exposure.jar"
  :ring {:handler exposure.handler/app
         :uberwar-name "exposure.war"}

  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets  {:assets
                   {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild {:builds {:app {:source-paths ["src/cljs" "src/cljc"]
                             :compiler {:output-to "target/cljsbuild/public/js/app.js"
                                        :output-dir "target/cljsbuild/public/js/out"
                                        :asset-path   "js/out"
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :less {:source-paths ["src/less"]
         :target-path "resources/public/css"}

  :profiles {:dev {:repl-options {:init-ns exposure.repl}

                   :dependencies [[ring/ring-mock "0.3.0"]
                                  [ring/ring-devel "1.4.0"]
                                  [lein-figwheel "0.5.0-2"]
                                  [org.clojure/clojurescript "1.7.170"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [pjstadig/humane-test-output "0.7.0"]]

                   :plugins [[lein-figwheel "0.5.0-2"]
                             [lein-ring "0.9.7"]
                             [org.clojure/clojurescript "1.7.170"]
                             [lein-less "1.7.5"]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :cljsbuild {:builds {:app {:figwheel {:on-jsload "exposure.core/mount-root"}
                                              :compiler {:main "exposure.core"
                                                         :source-map true
                                                         :source-map-timestamp true}}}}

                   :figwheel {:css-dirs ["resources/public/css"]
                              :ring-handler exposure.handler/app}

                   :env {:dev true
                         :host "http://localhost:3449"
                         :instagram-client-id "4b4d8befff9846ecb4491637e5674a07"
                         :instagram-client-secret "3b1ce6fa79134675bc8efbc6c9dd7258"}}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :prep-tasks [["less" "once"]
                                    "compile"
                                    ["cljsbuild" "once"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true
                       :cljsbuild {:jar true
                                   :builds {:app
                                            {:compiler {:main "exposure.core"
                                                        :optimizations :advanced
                                                        :closure-defines {goog.DEBUG false}
                                                        :pretty-print false}}}}}})
