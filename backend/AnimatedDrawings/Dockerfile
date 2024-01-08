FROM continuumio/miniconda3:latest

ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

COPY . /app
WORKDIR /app

RUN conda create --name animated_drawings python=3.8.13

SHELL ["conda", "run", "-n", "animated_drawings", "/bin/bash", "-c"]

# Install gcc and python3-dev
RUN apt-get update && apt-get install -y gcc python3-dev

# Install psutil
RUN pip install -e .

RUN apt-get update && apt-get install -y g++ default-jdk

RUN apt-get install -y libosmesa6-dev freeglut3-dev &&  \
    apt-get install -y libglfw3-dev libgles2-mesa-dev &&  \
    apt-get install -y libosmesa6

RUN export PYOPENGL_PLATFORM=osmesa

CMD ["conda", "run", "-n", "animated_drawings", "python3", "-m", "flask", "run", "--host=0.0.0.0"]
