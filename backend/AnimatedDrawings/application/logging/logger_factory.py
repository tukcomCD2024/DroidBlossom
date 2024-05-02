import logging
import sys

from application.config.logger_config import LoggerConfig


class LoggerFactory:

    @staticmethod
    def get_logger(name: str) -> logging.Logger:
        """
        기본 로거를 생성해준다.
        :param name: 로거의 이름
        :return: 설정된 로거를 반환한다.
        """
        logger = logging.getLogger(name)
        logger.setLevel(LoggerConfig.LOGGING_LEVEL)

        LoggerFactory._setup_handler(LoggerConfig.FORMAT_STRING,
                                     LoggerConfig.LOGGING_LEVEL,
                                     logger,
                                     LoggerConfig.APPLICATION_OUTPUT_FILE_PATH)

        return logger

    @staticmethod
    def setup_logger(logger: logging.Logger, output_file_path: str) -> None:
        """
        파라미터로 받은 로거에 포맷터를 설정한다.
        :param logger: 설정할 로거
        :param output_file_path: 로그를 기록할 파일의 경로
        """
        LoggerFactory._setup_handler(LoggerConfig.FORMAT_STRING,
                                     LoggerConfig.LOGGING_LEVEL,
                                     logger,
                                     output_file_path)

    @staticmethod
    def _setup_handler(format_string, level, logger, output_file_path):
        formatter = logging.Formatter(format_string)

        stream_handler = logging.StreamHandler(sys.stdout)
        stream_handler.setLevel(level)
        stream_handler.setFormatter(formatter)

        file_handler = logging.FileHandler(filename=output_file_path)
        file_handler.setLevel(level)
        file_handler.setFormatter(formatter)

        logger.addHandler(file_handler)
        logger.addHandler(stream_handler)
