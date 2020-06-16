(ns pwdgenerator-reframe.domain
  (:require
    [clojure.string :refer [join]]
    [goog.string :refer [format]]))

(def password-stats
  [(fn [s]
     (format "%d characters total length"
             (->> (vec s)
                  count)))
   (fn [s]
     (format "%d uppercase alpha characters"
             (->> (vec s)
                  (map #(some #{%} (vec "ABCDEFGHIJKLMNOPQRSTUVWXYZ")))
                  (remove nil?)
                  count)))
   (fn [s]
     (format "%d lowercase alpha characters"
             (->> (vec s)
                  (map #(some #{%} (vec "abcdefghijklmnopqrstuvwxyz")))
                  (remove nil?)
                  count)))
   (fn [s]
     (format "%d number characters"
             (->> (vec s)
                  (map #(some #{%} (vec "0123456789")))
                  (remove nil?)
                  count)))
   (fn [s]
     (format "%d symbol characters"
             (->> (vec s)
                  (map #(some #{%} (vec " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")))
                  (remove #(not (nil? %)))
                  count)))
   (fn [s]
     (format "%d spaces"
             (->> (vec s)
                  (map #(= % \space))
                  (remove false?)
                  count)))
   ])

(defn random-char [s]
  (nth s (rand-int (count s))))

(defn random-string [len s]
  (let [length (js/parseInt len)]
    (apply str (take length (repeatedly #(random-char s))))))

(defn uppercase-word [params]
  (random-string (:no_uppercase_alpha params) (:uppercase params)))

(defn lowercase-word [params]
  (random-string (:no_lowercase_alpha params) (:lowercase params)))

(defn numerics-symbols-word [params]
  (apply str
         (shuffle
           (seq
             (str (random-string (:no_numerics params) (:numerics params))
                  (random-string (:no_symbols params) (:symbols params)))))))

(defn all-words [params]
  (let [generators [uppercase-word lowercase-word numerics-symbols-word]
        words (js/parseInt (:no_words params))]
    (shuffle
      (take words (repeatedly #((nth generators (rand-int (count generators))) params))))))

(defn generate-pw [params]
  (join (:word_separator params) (all-words params)))
