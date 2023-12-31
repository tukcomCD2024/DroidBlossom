from flask import request, jsonify
from flask_restx import Resource, fields
from celery import chain
from tasks import make_animation
from tasks import success
from controller import AnimatedDrawings

output_task_id = AnimatedDrawings.inherit(
    'Output Task',
    {
      'success': fields.String(description='status'),
      'message': fields.String(desription='application message')
    }
)


@AnimatedDrawings.route('/image_to_animation')
class GenerateAnimatedDrawings(Resource):
  @AnimatedDrawings.response(200, "Success", output_task_id)
  def post(self):
    try:
      img_fn = request.files['img_fn'].read()
      char_anno_dir = request.form.get('char_anno_dir')
      motion_cfg_fn = request.form.get('motion_cfg_fn',
                                       'examples/config/motion/dab.yaml')
      retarget_cfg_fn = request.form.get('retarget_cfg_fn',
                                         'examples/config/retarget/fair1_ppf.yaml')

      AnimatedDrawings.logger.info("hi")

      chain(
          make_animation.s(img_fn, char_anno_dir, motion_cfg_fn,
                           retarget_cfg_fn).set(queue='animation_tasks'),
          success.s().set(queue='success_tasks')
      ).apply_async(
          ignore_result=False
      )

      return jsonify(
          {'success': True, 'message': 'Animation created successfully'})
    except Exception as e:
      return jsonify({'success': False, 'error': str(e)})
