(ns exposure.config)

(def debug?
  ^boolean js/goog.DEBUG)

(def gmaps-api-key "AIzaSyDTy9FH8KmR282jXI3Nn-nOX3jzuDXZ8KQ")

(when debug?
  (enable-console-print!))
