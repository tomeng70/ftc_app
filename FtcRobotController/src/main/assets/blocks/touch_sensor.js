/**
 * @fileoverview FTC robot blocks related to touch sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

createTouchSensorDropdown_ = function() {
  // The identifier must match the identifier used in BlocksOpMode.java.
  var TOUCH_SENSOR_IDENTIFIER = 'sensorTouch';
  var CHOICES = [
      ['FtcTouchSensor1', TOUCH_SENSOR_IDENTIFIER]];
  return new Blockly.FieldDropdown(CHOICES);
};

Blockly.Blocks['touchSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['IsPressed', 'IsPressed']];
    this.setOutput(true);
    this.appendDummyInput()
        .appendField(createTouchSensorDropdown_(), 'TOUCH_SENSOR')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['IsPressed', 'Returns true if the touch sensor is pressed.']];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PROP');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
    this.setColour(151);
  }
};

Blockly.JavaScript['touchSensor_getProperty'] = function(block) {
  var touchSensorIdentifier = block.getFieldValue('TOUCH_SENSOR');
  var property = block.getFieldValue('PROP');
  var code = touchSensorIdentifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
