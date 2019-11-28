(ns adb-fingerprint.core
  (:require [clojure.string :as str])
  (:import (java.util Base64)
           (java.security MessageDigest)))

(defn read-pub-key! []
  (slurp (str (System/getenv "HOME")
              "/.android/adbkey.pub")))

(defn decode-base64 [^String encoded]
  (let [decoded (.decode (Base64/getDecoder) encoded)]
    decoded))

(defn print-then-return [x] (println x) x)

(defn encode-md5 [bytes]
  (let [md5 (MessageDigest/getInstance "MD5")
        digested (.digest md5 bytes)]
    (format "%032x" (BigInteger. 1 digested))))

(defn split-by-2 [s]
  (re-seq #".{1,2}" s))

(defn join-with-colon [coll]
  (str/join ":" coll))

(defn -main []
  (-> (read-pub-key!)
      (str/split #" ")
      (first)
      (decode-base64)
      (encode-md5)
      (str/upper-case)
      (split-by-2)
      (join-with-colon)
      (println)))
