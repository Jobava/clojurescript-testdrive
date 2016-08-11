(ns gears2.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/gears2/core.cljs. Go ahead and edit it and see reloading in action.")

(def pi 3.141592653589793)

(def twopi (* pi 2))

(defn pin [n] (/ pi n))

(defn sin [x] (. js/Math (sin x)))

(defn cos [x] (. js/Math (cos x)))

(defn atan2 [y x] (. js/Math (atan2 y x)))

(defn sqrt [x] (. js/Math (sqrt x)))

(defn rotate [[x y] alpha]
  "rotate a point around the origin by angle in radians"
  [(- (* x (cos alpha)) (* y (sin alpha)))
   (+ (* x (sin alpha)) (* y (cos alpha)))])

(defn translate [[x y] [xd yd]]
  "translate a point by another point"
  [(+ x xd) (+ y yd)])

(def origin [0 0])

(defn deg-to-rad [deg]
  "convert an angle in degrees into radians"
  (* pi (/ deg 180)))

(defn rad-to-deg [rad]
  "convert an angle in radians to degrees"
  (* 180 (/ rad pi)))

(defn point-on-circle [alpha]
  "get a point on a circle at a given alpha angle"
  [(cos alpha) (sin alpha)])

(defn poly-point [n sides]
  "get the n-th point in a regular polygon of a number of sides"
  (let [inverse-sides (/ 1 sides)
        alpha (* n twopi inverse-sides)]
    (point-on-circle alpha)))

(defn poly-points [sides]
  "get all regular polygon points for a polygon of a number of sides"
  (for [i (range 0 sides)] (poly-point i sides)))

(defn flat-poly-points [sides]
  "get a flattened list of coordinates as obtained from poly-points"
  (flatten (poly-points sides)))

(defn scaled-poly-points [sides radius]
  (for [i (flat-poly-points sides)] (+ (* 2 radius) (* i radius))))

(defn svg-polygon [sides radius]
  "a representation for a regular polygon in svg and hiccup"
  [:svg
   {:width 300
    :height 300}
   [:g
    [:polygon {:points [(scaled-poly-points sides radius)]}
     :stroke "black"
     :stroke-width 1]]])

(defonce app-state (atom {:sides 6 :scale 50 :center-x 100 :center-y 100}))

(defn poly []
  (svg-polygon (:sides @app-state) (:scale @app-state)) )

(defn slider []
  [:input {:type "range" 
           :value (:sides @app-state) :min 3 :max 20
           :style {:width "50%"}
           :on-change (
                       fn [e]
                       (swap! app-state assoc :sides (.-target.value e))
                       )
           }]
  )

(defn slider-text []
  [:input {:type "text"
           :value (:sides @app-state)}])

(defn render-component-at [component id]
  (reagent/render-component [component]
                            (. js/document (getElementById id))))

(render-component-at poly "poly")
(render-component-at slider "slider")
(render-component-at slider-text "slider-text")

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
