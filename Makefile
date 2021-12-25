RELEASE_VERSION ?= latest

SUBDIRS=kafka-consumer kafka-producer
DOCKER_TARGETS=docker_build docker_push docker_tag docker_amend_manifest docker_push_manifest docker_delete_manifest

all: $(SUBDIRS)
build: $(SUBDIRS)
clean: $(SUBDIRS)
$(DOCKER_TARGETS): $(SUBDIRS)

$(SUBDIRS):
	$(MAKE) -C $@ $(MAKECMDGOALS)

.PHONY: all $(SUBDIRS) $(DOCKER_TARGETS)
