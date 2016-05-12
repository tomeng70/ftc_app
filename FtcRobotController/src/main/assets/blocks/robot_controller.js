/**
 * @fileoverview FTC robot blocks related to telemetry.
 * @author lizlooney@google.com (Liz Looney)
 */

createRobotControllerDropdown_ = function() {
  // The identifier must match the identifier used in BlocksOpMode.java.
  var ROBOT_CONTROLLER_IDENTIFIER = 'robotController';
  var CHOICES = [
      ['FtcRobotController1', ROBOT_CONTROLLER_IDENTIFIER]];
  return new Blockly.FieldDropdown(CHOICES);
};

Blockly.Blocks['robotController_telemetryAddNumericData'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createRobotControllerDropdown_(), 'ROBOT_CONTROLLER')
        .appendField('TelemetryAddNumericData');
    this.appendValueInput('KEY')
        .appendField('key')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('NUMBER')
        .appendField('number')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(289);
    this.setTooltip('Add a numeric data point.');
  }
};

Blockly.JavaScript['robotController_telemetryAddNumericData'] = function(block) {
  var robotControllerIdentifier = block.getFieldValue('ROBOT_CONTROLLER');
  var key = Blockly.JavaScript.valueToCode(
      block, 'KEY', Blockly.JavaScript.ORDER_COMMA);
  var number = Blockly.JavaScript.valueToCode(
      block, 'NUMBER', Blockly.JavaScript.ORDER_COMMA);
  return robotControllerIdentifier +
      '.addData(' + key + ', ' + number + ');\n';
};

Blockly.Blocks['robotController_telemetryAddTextData'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createRobotControllerDropdown_(), 'ROBOT_CONTROLLER')
        .appendField('TelemetryAddTextData');
    this.appendValueInput('KEY')
        .appendField('key')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('TEXT')
        .appendField('text')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(289);
    this.setTooltip('Add a text data point.');
  }
};

Blockly.JavaScript['robotController_telemetryAddTextData'] = function(block) {
  var robotControllerIdentifier = block.getFieldValue('ROBOT_CONTROLLER');
  var key = Blockly.JavaScript.valueToCode(
      block, 'KEY', Blockly.JavaScript.ORDER_COMMA);
  var text = Blockly.JavaScript.valueToCode(
      block, 'TEXT', Blockly.JavaScript.ORDER_COMMA);
  return robotControllerIdentifier +
      '.addData(' + key + ', ' + text + ');\n';
};
