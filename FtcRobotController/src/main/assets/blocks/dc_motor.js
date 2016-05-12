/**
 * @fileoverview FTC robot blocks related to DC motors.
 * @author lizlooney@google.com (Liz Looney)
 */

createMotorDropdown_ = function() {
  // The identifiers must match the identifiers used in BlocksOpMode.java.
  var LEFT_MOTOR_IDENTIFIER = 'motorL';
  var RIGHT_MOTOR_IDENTIFIER = 'motorR';
  var CHOICES = [
      ['FtcDcMotorLeft', LEFT_MOTOR_IDENTIFIER],
      ['FtcDcMotorRight', RIGHT_MOTOR_IDENTIFIER]];
  return new Blockly.FieldDropdown(CHOICES);
};

Blockly.Blocks['dcMotor_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
        ['Mode', 'Mode'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition']];
    this.appendValueInput('VALUE')
        .appendField('set')
        .appendField(createMotorDropdown_(), 'MOTOR')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Sets the direction for the motor.'],
        ['Mode', 'Sets the run mode for the motor.'],
        ['Power', 'Sets the power for the motor. ' +
            'Power values must be between -1.0 and 1.0.'],
        ['TargetPosition', 'Sets the target position for the motor.']];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PROP');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
    this.setColour(147);
  }
};

Blockly.JavaScript['dcMotor_setProperty'] = function(block) {
  var motorIdentifier = block.getFieldValue('MOTOR');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return motorIdentifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['dcMotor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CurrentPosition', 'CurrentPosition'],
        ['Direction', 'Direction'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
        ['Mode', 'Mode']];
    this.setOutput(true);
    this.appendDummyInput()
        .appendField(createMotorDropdown_(), 'MOTOR')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CurrentPosition', 'Gets the current position of the motor.'],
        ['Direction', 'Gets the direction of the motor.'],
        ['Mode', 'Gets the run mode of the motor.'],
        ['Power', 'Gets the power of the motor.'],
        ['TargetPosition', 'Gets the target position of the motor.']];
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

Blockly.JavaScript['dcMotor_getProperty'] = function(block) {
  var motorIdentifier = block.getFieldValue('MOTOR');
  var property = block.getFieldValue('PROP');
  var code = motorIdentifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

// Constants

Blockly.Blocks['dcMotor_constant_runMode'] = {
  init: function() {
    var RUN_MODE_CHOICES = [
        ['RunMode_RUN_USING_ENCODERS', 'RUN_USING_ENCODERS'],
        ['RunMode_RUN_WITHOUT_ENCODERS', 'RUN_WITHOUT_ENCODERS'],
        ['RunMode_RUN_TO_POSITION', 'RUN_TO_POSITION'],
        ['RunMode_RESET_ENCODERS', 'RESET_ENCODERS']];
    this.setOutput(true);
    this.appendDummyInput()
        .appendField(createMotorDropdown_(), 'MOTOR')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(RUN_MODE_CHOICES), 'RUN_MODE');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['RUN_USING_ENCODERS', 'The run mode value RUN_USING_ENCODERS.'],
        ['RUN_WITHOUT_ENCODERS', 'The run mode value RUN_WITHOUT_ENCODERS.'],
        ['RUN_TO_POSITION', 'The run mode value RUN_TO_POSITION.'],
        ['RESET_ENCODERS', 'The run mode value RESET_ENCODERS.']];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('RUN_MODE');
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

Blockly.JavaScript['dcMotor_constant_runMode'] = function(block) {
  var runMode = block.getFieldValue('RUN_MODE');
  return [runMode, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['dcMotor_constant_direction'] = {
  init: function() {
    var DIRECTION_CHOICES = [
        ['Direction_REVERSE', 'REVERSE'],
        ['Direction_FORWARD', 'FORWARD']];
    this.setOutput(true);
    this.appendDummyInput()
        .appendField(createMotorDropdown_(), 'MOTOR')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(DIRECTION_CHOICES), 'DIRECTION');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['REVERSE', 'The direction value REVERSE.'],
        ['FORWARD', 'The direction value FORWARD.']];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('DIRECTION');
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

Blockly.JavaScript['dcMotor_constant_direction'] = function(block) {
  var direction = block.getFieldValue('DIRECTION');
  return [direction, Blockly.JavaScript.ORDER_ATOMIC];
};
