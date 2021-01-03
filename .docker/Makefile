SHELL = /bin/bash

VERSION ?= $(shell git describe --tags --abbrev=0)
IMAGE ?= paulosalonso/research
ROOTPATH ?= $(shell git rev-parse --show-toplevel)
TMP_DIR := "$(shell mktemp -d $(ROOTPATH)/tmp.build.XXXXXXXXXX)"
ARTIFACTS ?= "$(shell git rev-parse --show-toplevel)/target/research.jar"
DOCKERPATH ?= "$(shell git rev-parse --show-toplevel)/.docker"
BRANCH ?= $(shell git name-rev --name-only HEAD)

# Define here the list of images that should be builded
LIST_OF_IMAGES_TO_BUILD := $(IMAGE)
export

# Rule "build:snapshot"
.PHONY: build\:snapshot
build\:snapshot:
	cp -R $(ARTIFACTS) $(TMP_DIR)
	cp -R $(DOCKERPATH)/* $(TMP_DIR)
	cd $(TMP_DIR)
	docker build --force-rm \
		-t "$(IMAGE)":snapshot \
		--build-arg CURRENT_BRANCH=$(BRANCH) \
		$(TMP_DIR)
	sleep 0.2
	rm -rf $(TMP_DIR)

# Rule "build"
.PHONY: build
build:
	cp -R $(ARTIFACTS) $(TMP_DIR)
	cp -R $(DOCKERPATH)/* $(TMP_DIR)
	cd $(TMP_DIR)
	docker build --force-rm \
		--no-cache -t $(IMAGE):$(VERSION) \
		--build-arg CURRENT_BRANCH=$(BRANCH) \
		$(TMP_DIR)
	sleep 0.2
	rm -rf $(TMP_DIR)
