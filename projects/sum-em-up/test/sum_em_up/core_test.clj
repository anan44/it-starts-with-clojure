(ns sum-em-up.core-test
  (:require [clojure.test :refer :all]
            [sum-em-up.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest str->long-test
  (testing "Casting text to numbers"
    (is (= 15 (str->long "15")))
    (is (= -100 (str->long "-100")))))

(deftest sum-test
  (testing "Adding up numbers in collections of different lengths and types."
    (is (= 10 (sum [5 5])))
    (is (= 0 (sum '(10 -5 -3 -1 -1))))
    (is (= 100 (sum #{25 75})))
    (is (= 0 (sum [])))))

(deftest split-words-test
  (testing "Tests splitting words from string"
    (is (= ["1" "2" "3"] (split-words "1 2 3")))
    (is (= ["Hello" "World" (split-words "Hello World")]))))
