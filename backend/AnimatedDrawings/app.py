import os
from logging.config import dictConfig
from flask import Flask
from flask_restx import Api


def create_app():
  dictConfig({
    'version': 1,
    'formatters': {
      'default': {
        'format': '[%(asctime)s] %(levelname)s in %(module)s: %(message)s',
      }
    },
    'handlers': {
      'file': {
        'level': 'INFO',
        'class': 'logging.handlers.RotatingFileHandler',
        'filename': './flask/application.log',
        'maxBytes': 1024 * 1024 * 5,  # 5 MB
        'backupCount': 5,
        'formatter': 'default',
      },
    },
    'root': {
      'level': 'INFO',
      'handlers': ['file']
    }
  })

  app = Flask(__name__)

  api = Api(app, version='v1')

  from controller import AnimatedDrawingsController
  api.add_namespace(AnimatedDrawingsController.AnimatedDrawings,
                    "/ai_api/animation")

  return app
