; template.clj
; to get you started

(import processing.core.PApplet)

(defn pulsedSize [frame maxSize speed]
  (- maxSize (mod (* frame speed) maxSize)))

(def p
  (proxy [PApplet] []
    (setup []
      (println "setup")
      (.size this 300 300)
    )
    (draw []
      (println (.frameCount this))
      (.background this 0)
      (def size (pulsedSize (.frameCount this) (.width this) 4))
      (.fill this size)
      (.ellipse this (/ (.width this) 2) (/ (.height this) 2) size size)
    )
))

(.runSketch p)