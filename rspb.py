#! /usr/bin/env python
# -*- coding: utf-8 -*-

import RPi.GPIO as GPIO
import time
import sys

BUZZER_GPIO = 2
BUTTON_GPIO = 14

DELAY = 0.1
DOT_DELAY = DELAY * 1
DASH_DELAY = DELAY * 3
SYMBOL_DELAY = DELAY * 1
PAUSE_DELAY = DELAY * 2

WORD_PAUSE_DELAY = DELAY * 6

MORSE_CODES = {
    'а': '.-', 'б': '-...', 'в': '.--', 'г': '--.', 'д': '-..', 'е': '.', 'ж': '...-', 'з': '--..',
    'и': '..', 'й': '.---', 'к': '-.-', 'л': '.-..', 'м': '--', 'н': '-.', 'о': '---', 'п': '.--.',
    'р': '.-.', 'с': '...', 'т': '-', 'у': '..-', 'ф': '..-.', 'х': '....',
    'ц': '-.-.', 'ч': '---.', 'ш': '----', 'щ': '--.-', 'ъ': '.--.-.', 'ы': '-.--', 'ь': '-..-',
    'э': '..-..', 'ю': '..--', 'я': '.-.-',
    "0": "-----", "1": ".----", "2": "..---", "3": "...--", "4": "....-", "5": ".....", "6": "-....",
    "7": "--...", "8": "---..", "9": "----.",
    " ": "/", ".": ".-.-.-", ",": "--..--", ":": "---...", ";": "-.-.-.", "?": "..--..",
    "-": "-....-", "(": "-.--.", ")": "-.--.-", "'": ".----.", "=": "-...-", "+": ".-.-.",
    "/": " ", "@": ".--.-."
}

def setup():
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(BUZZER_GPIO, GPIO.OUT)
    GPIO.setup(BUTTON_GPIO, GPIO.IN)

def beep(t):
    GPIO.output(BUZZER_GPIO, GPIO.HIGH)
    time.sleep(t)
    GPIO.output(BUZZER_GPIO, GPIO.LOW)


def morse(symbols):
    for s in symbols:
        if s == ".":
            beep(DOT_DELAY)
            time.sleep(SYMBOL_DELAY)
        elif s == "-":
            beep(DASH_DELAY)
            time.sleep(SYMBOL_DELAY)
        elif s == " ":
            time.sleep(PAUSE_DELAY)
        elif s == "/":
            time.sleep(WORD_PAUSE_DELAY)


def text_to_morse(text):
    code = ""
    for s in text:
        code += MORSE_CODES[s] + " "
    return code


def read_text():
    text = []
    with open("text.txt", 'r', encoding='utf-8' ) as file:
        text = file.read().lower().splitlines()
    return text


if __name__ == '__main__':
    setup()

    text = read_text()
    print(text)

    try:
        while len(text) > 0:
            if not GPIO.input(BUTTON_GPIO):
                s = text.pop(0)
                print(s)
                code = text_to_morse(s)
                morse(code)
            time.sleep(DELAY)
        GPIO.cleanup()
    except KeyboardInterrupt:
        GPIO.cleanup()
