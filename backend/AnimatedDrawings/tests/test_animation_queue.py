import json
import unittest

from application.animation_queue import AnimationQueueController
from application.model.motion import Motion
from application.model.retarget import Retarget


class TestParseJson(unittest.TestCase):
    def setUp(self):
        self.controller = AnimationQueueController()

    def test_parse_json_valid(self):
        test_json = json.dumps({
            "memberId": 123,
            "memberName": "hello",
            "skinName": "갈릭이",
            "imageUrl": "https://example.com",
            "retarget": "FAIR",
            "motionName": "DAB"
        }).encode('utf-8')

        expected_result = {
            "memberId": "123",
            "memberName": "hello",
            "skinName": "갈릭이",
            "imageUrl": "https://example.com",
            "retarget": Retarget.FAIR.value,
            "motionName": Motion.DAB.value
        }

        result = self.controller.parse_body(test_json)
        self.assertEqual(result, expected_result)

    def test_parse_json_invalid_enum(self):
        test_json = json.dumps({
            "memberId": 123,
            "memberName": "hello",
            "skinName": "갈릭이",
            "imageUrl": "https://example.com",
            "retarget": "FAIR1",
            "motionName": "DAB1"
        }).encode('utf-8')

        with self.assertRaises(KeyError):
            self.controller.parse_body(test_json)

    def test_parse_json_invalid_format(self):
        test_json = """{"memberId": 123,
                     "memberName": "hello",
                     "skinName": "갈릭이",
                     "imageUrl": "https://example.com",
                     "retarget":"FAIR1"
                     "motionName": "DAB1"}
                     """.encode('utf-8')

        with self.assertRaises(json.JSONDecodeError):
            self.controller.parse_body(test_json)


# 테스트 실행
if __name__ == '__main__':
    unittest.main()
