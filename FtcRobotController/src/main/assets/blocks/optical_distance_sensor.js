/**
 * @fileoverview FTC robot blocks related to optical distance sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

createOpticalDistanceSensorDropdown_ = function() {
  // The identifier must match the identifier used in BlocksOpMode.java.
  var OPTICAL_DISTANCE_SENSOR_IDENTIFIER = 'sensorOpticalDistance';
  var CHOICES = [
      ['FtcOpticalDistanceSensor1', OPTICAL_DISTANCE_SENSOR_IDENTIFIER]];
  return new Blockly.FieldDropdown(CHOICES);
};

Blockly.Blocks['opticalDistanceSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['LightDetected', 'LightDetected'],
        ['LightDetectedRaw', 'LightDetectedRaw']];
    this.setOutput(true);
    this.appendDummyInput()
        .appendField(createOpticalDistanceSensorDropdown_(), 'OPTICAL_DISTANCE_SENSOR')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['LightDetected', 'Gets the amount of light detected by the optical distance sensor. ' +
            'The value is between 0.0 and 1.0.'],
        ['LightDetectedRaw', 'Gets the amount of light detected by the optical distance sensor. ' +
            'The value is between 0.0 and 1.0.']];
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

Blockly.JavaScript['opticalDistanceSensor_getProperty'] = function(block) {
  var opticalDistanceSensorIdentifier = block.getFieldValue('OPTICAL_DISTANCE_SENSOR');
  var property = block.getFieldValue('PROP');
  var code = opticalDistanceSensorIdentifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
