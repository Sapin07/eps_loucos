SRC_DIR := src
BIN_DIR := bin
MAIN_CLASS := game.Main

SOURCES := $(shell find $(SRC_DIR) -name "*.java")

.PHONY: all build run clean

all: build

build:
	mkdir -p $(BIN_DIR)
	javac -d $(BIN_DIR) $(SOURCES)

run: build
	java -cp $(BIN_DIR) $(MAIN_CLASS)

clean:
	rm -rf $(BIN_DIR)/*
