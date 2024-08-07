FROM continuumio/miniconda3:24.1.2-0

ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

COPY . /app
RUN mkdir -p /app/application/capsuleSkin
WORKDIR /app

RUN conda install python=3.8.13 -y

# Install gcc and python3-dev
RUN apt-get clean
RUN apt-get update && \
    apt-get install --no-install-recommends -y \
    supervisor gcc build-essential \
    libosmesa6-dev freeglut3-dev  \
    libglfw3-dev libgles2-mesa-dev \
    libosmesa6 \
    libglib2.0-0 libsm6 libxrender1 libxext6 \
    ffmpeg libavcodec-extra \
    && rm -rf /var/lib/apt/lists/*

COPY supervisord.conf /etc/supervisor/supervisord.conf

# Install psutil
RUN pip install -e .

WORKDIR /app/application

RUN mkdir log

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/supervisord.conf"]
