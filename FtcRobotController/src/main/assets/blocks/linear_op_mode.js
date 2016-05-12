/**
 * @fileoverview FTC robot blocks related to linear op mode.
 * @author lizlooney@google.com (Liz Looney)
 */

createLinearOpModeDropdown_ = function() {
  // The identifier must match the identifier used in BlocksOpMode.java.
  var LINEAR_OP_MODE_IDENTIFIER = 'linearOpMode';
  var CHOICES = [
      ['FtcLinearOpMode1', LINEAR_OP_MODE_IDENTIFIER]];
  return new Blockly.FieldDropdown(CHOICES);
};

Blockly.Blocks['linearOpMode_waitForStart'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLinearOpModeDropdown_(), 'LINEAR_OP_MODE')
        .appendField('WaitForStart');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(289);
    this.setTooltip('Wait until start has been pressed.');
  }
};

Blockly.JavaScript['linearOpMode_waitForStart'] = function(block) {
  var linearOpModeIdentifier = block.getFieldValue('LINEAR_OP_MODE');
  return linearOpModeIdentifier + '.waitForStart();\n';
};

Blockly.Blocks['linearOpMode_sleep'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLinearOpModeDropdown_(), 'LINEAR_OP_MODE')
        .appendField('Sleep');
    this.appendValueInput('MILLISECONDS')
        .appendField('milliseconds')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(289);
    this.setTooltip('Sleep for the given amount of milliseconds.');
  }
};

Blockly.JavaScript['linearOpMode_sleep'] = function(block) {
  var linearOpModeIdentifier = block.getFieldValue('LINEAR_OP_MODE');
  var millis = Blockly.JavaScript.valueToCode(
      block, 'MILLISECONDS', Blockly.JavaScript.ORDER_NONE);
  return linearOpModeIdentifier + '.sleep(' + millis + ');\n';
};

Blockly.Blocks['linearOpMode_opModeIsActive'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLinearOpModeDropdown_(), 'LINEAR_OP_MODE')
        .appendField('OpModeIsActive');
    this.setTooltip('Return true if this opMode is active.');
    this.setColour(298);
  }
};

Blockly.JavaScript['linearOpMode_opModeIsActive'] = function(block) {
  var linearOpModeIdentifier = block.getFieldValue('LINEAR_OP_MODE');
  var code = linearOpModeIdentifier + '.opModeIsActive()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
