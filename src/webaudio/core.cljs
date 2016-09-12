(ns webaudio.core)

;; I think these are the same
(enable-console-print!)
(def console (.-log js/console))
;(def console (.-log js/addEventListener))

;; webaudio 
(def ctx (js/AudioContext.))
(def destination (.-destination ctx))

(defn create-osc [type freq]
  (let [osc (.createOscillator ctx)]
    (set! (.-type osc) type)
    (set! (.-value (.-frequency osc)) freq)
    osc))

(defn create-gain [level]
  (let [gain (.createGain ctx)]
    (set! (.-value (.-gain gain)) level)
    gain))

(defn env [atk-time, dec-time, sus-time, rel-time,
           atk-val, dec-val, sus-val, rel-val]
  (let [gain (create-gain 0.5)]
    (.setTargetAtTime (.-gain gain) 0.001 (.-currentTime ctx) 0.001)
    (.exponentialRampToValueAtTime (.-gain gain) atk-val (+ (.-currentTime ctx) atk-time))
    (.exponentialRampToValueAtTime (.-gain gain) dec-val (+ (.-currentTime ctx) dec-time))
    (.exponentialRampToValueAtTime (.-gain gain) sus-val (+ (.-currentTime ctx) sus-time))
    (.exponentialRampToValueAtTime (.-gain gain) rel-val (+ (.-currentTime ctx) rel-time))
    gain))

(def envelope (env 2.5 0.4 0.6 2.3        ;; times
                   0.7 0.6 0.6 0.001))    ;; values

(defn on! [type freq gain-value env]
  (let [osc (create-osc type freq)
        gain (create-gain gain-value)]
    (.connect osc gain)
    (.connect gain env)
    (.connect env destination)
    (.start osc (.-currentTime ctx))
    (.stop osc (+ (.-currentTime ctx) 5.9))))

(on! "square" 220 0.5 envelope)





(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )










