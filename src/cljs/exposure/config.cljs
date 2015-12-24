(ns exposure.config)

(def debug?
  ^boolean js/goog.DEBUG)

(def mapbox-token "pk.eyJ1Ijoib3Rhbm4iLCJhIjoicnpIQTFqSSJ9.XtKI-RRhg9RLFzXzPyadqw")

(when debug?
  (enable-console-print!))
