; template.clj
; to get you started

(import processing.core.PApplet)

(defn pulsedSize [frame maxSize speed]
  (- maxSize (mod (* frame speed) maxSize)))

(def p
  (proxy [PApplet] []
    (setup [] 
      (println "setup")
      (. this size 300 300)
    )
    (draw [] 
      (println (. this frameCount))
      (. this background 0)
      (def size (pulsedSize (. this frameCount) (. this width) 4))
      (. this fill size)
      (. this ellipse (/ (. this width) 2) (/ (. this height) 2) size size)
    )
))

(. p runSketch)
