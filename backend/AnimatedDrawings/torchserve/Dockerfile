# syntax = docker/dockerfile:1.2

FROM continuumio/miniconda3:24.1.2-0

# install os dependencies
RUN mkdir -p /usr/share/man/man1
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install --no-install-recommends -y \
    ca-certificates \
    curl \
    python3-pip \
    vim \
    sudo \
    default-jre \
    git \
    gcc \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# install python dependencies
RUN pip install openmim==0.3.9
RUN pip install torch==2.0.0
RUN mim install mmcv-full==1.7.0
RUN pip install mmdet==2.27.0
RUN pip install torchserve==0.9.0

# bugfix for xtcocoapi, an mmpose dependency
RUN git clone https://github.com/hjessmith/xtcocoapi-bugfix.git
WORKDIR xtcocoapi-bugfix
RUN pip install -r requirements.txt
RUN python setup.py install
WORKDIR /
RUN pip install mmpose==0.29.0

# prep torchserve
RUN mkdir -p /home/torchserve/model-store
RUN wget https://github.com/facebookresearch/AnimatedDrawings/releases/download/v0.0.1/drawn_humanoid_detector.mar -P /home/torchserve/model-store/
RUN wget https://github.com/facebookresearch/AnimatedDrawings/releases/download/v0.0.1/drawn_humanoid_pose_estimator.mar -P /home/torchserve/model-store/
COPY config.properties.template /home/torchserve/config.properties.template
COPY log4j2.xml /home/torchserve/log4j2.xml

ENV MAX_HEAP_SIZE=8g
ENV INIT_HEAP_SIZE=4g

CMD sed "s/{{MAX_HEAP_SIZE}}/$MAX_HEAP_SIZE/g; s/{{INIT_HEAP_SIZE}}/$INIT_HEAP_SIZE/g" \
    /home/torchserve/config.properties.template > /home/torchserve/config.properties && \
    /opt/conda/bin/torchserve --start --ts-config /home/torchserve/config.properties && \
    sleep infinity
