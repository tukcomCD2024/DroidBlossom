import logging
import sys


class LoggerFactory:

    @staticmethod
    def get_logger(
        name: str,
        output_file_path: str = '/var/log/application.log',
        level: int = logging.INFO,
        format_string: str = '%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    ) -> logging.Logger:
        """
        기본 로거를 생성해준다.
        :param name: 로거의 이름
        :param output_file_path: 로그를 기록할 파일의 경로
        :param level: 로깅의 레벨
        :param format_string: 로깅의 포맷팅
        :return: 설정된 로거를 반환한다.
        """
        if output_file_path is None or output_file_path == '':
            output_file_path = '/var/log/application.log'

        logger = logging.getLogger(name)
        logger.setLevel(level)

        LoggerFactory._setup_handler(format_string, level, logger,
                                     output_file_path)

        return logger

    @staticmethod
    def setup_logger(
        logger: logging.Logger,
        output_file_path: str = '/var/log/celeryd.log',
        level: int = logging.WARNING,
        format_string: str = '%(asctime)s - %(name)s - %(levelname)s'
    ) -> None:
        """
        파라미터로 받은 로거에 포맷터를 설정한다.
        :param logger: 설정할 로거
        :param output_file_path: 로그를 기록할 파일의 경로
        :param level: 로깅의 레벨
        :param format_string: 로깅의 포맷팅
        """
        LoggerFactory._setup_handler(format_string, level, logger,
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
