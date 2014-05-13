package com.puresoltechnologies.parsers.ust.eval;

import java.math.BigDecimal;

public class Value implements Comparable<Value> {

	private final String typeName;
	private final String stringValue;
	private final BigDecimal numericalValue;
	private final long integerValue;
	private final boolean booleanValue;
	private final int bitSize;
	private final boolean isNull;
	private final ValueType type;

	public Value() {
		type = ValueType.UNSPECIFIED;
		typeName = "";
		stringValue = "";
		integerValue = 0;
		numericalValue = BigDecimal.valueOf(0.0);
		booleanValue = false;
		bitSize = Integer.SIZE;
		isNull = true;
	}

	public Value(String typeName, String s) {
		type = ValueType.STRING;
		this.typeName = typeName;
		isNull = (s == null);
		stringValue = s;
		integerValue = 0;
		numericalValue = BigDecimal.ZERO;
		if (!isNull) {
			booleanValue = !s.isEmpty();
		} else {
			booleanValue = false;
		}
		bitSize = 0;
	}

	public Value(String typeName, boolean b) {
		type = ValueType.BOOLEAN;
		this.typeName = typeName;
		stringValue = String.valueOf(b);
		integerValue = (b ? 1 : 0);
		numericalValue = BigDecimal.valueOf(integerValue);
		booleanValue = b;
		bitSize = Integer.SIZE;
		isNull = false;
	}

	public Value(String typeName, byte b) {
		type = ValueType.INTEGER;
		this.typeName = typeName;
		stringValue = String.valueOf(b);
		integerValue = b;
		numericalValue = BigDecimal.valueOf(b);
		booleanValue = (b != 0);
		bitSize = Integer.SIZE;
		isNull = false;
	}

	public Value(String typeName, short s) {
		type = ValueType.INTEGER;
		this.typeName = typeName;
		stringValue = String.valueOf(s);
		integerValue = s;
		numericalValue = BigDecimal.valueOf(s);
		booleanValue = (s != 0);
		bitSize = Integer.SIZE;
		isNull = false;
	}

	public Value(String typeName, int i) {
		type = ValueType.INTEGER;
		this.typeName = typeName;
		stringValue = String.valueOf(i);
		integerValue = i;
		numericalValue = BigDecimal.valueOf(i);
		booleanValue = (i != 0);
		bitSize = Integer.SIZE;
		isNull = false;
	}

	public Value(String typeName, long l) {
		type = ValueType.INTEGER;
		this.typeName = typeName;
		stringValue = String.valueOf(l);
		integerValue = l;
		numericalValue = BigDecimal.valueOf(l);
		booleanValue = (l != 0);
		bitSize = Integer.SIZE;
		isNull = false;
	}

	public Value(String typeName, double d) {
		type = ValueType.NUMERICAL;
		this.typeName = typeName;
		stringValue = String.valueOf(d);
		integerValue = (int) d;
		numericalValue = BigDecimal.valueOf(d);
		booleanValue = (d != 0.0);
		bitSize = Double.SIZE;
		isNull = false;
	}

	public Value(String typeName, float f) {
		type = ValueType.NUMERICAL;
		this.typeName = typeName;
		stringValue = String.valueOf(f);
		integerValue = (int) f;
		numericalValue = BigDecimal.valueOf(f);
		booleanValue = (f != 0.0);
		bitSize = Float.SIZE;
		isNull = false;
	}

	public Value(String typeName, int bitSize, BigDecimal d) {
		type = ValueType.NUMERICAL;
		this.typeName = typeName;
		this.bitSize = bitSize;
		isNull = (d == null);
		if (!isNull) {
			stringValue = String.valueOf(d);
			integerValue = d.intValue();
			numericalValue = d;
			booleanValue = (d.doubleValue() != 0.0);
		} else {
			stringValue = null;
			integerValue = 0;
			numericalValue = null;
			booleanValue = false;
		}
	}

	@Override
	public String toString() {
		return stringValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bitSize;
		result = prime * result + (booleanValue ? 1231 : 1237);
		result = prime * result + (int) (integerValue ^ (integerValue >>> 32));
		result = prime * result + (isNull ? 1231 : 1237);
		result = prime * result
				+ ((numericalValue == null) ? 0 : numericalValue.hashCode());
		result = prime * result
				+ ((stringValue == null) ? 0 : stringValue.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Value other = (Value) obj;
		if (bitSize != other.bitSize)
			return false;
		if (booleanValue != other.booleanValue)
			return false;
		if (integerValue != other.integerValue)
			return false;
		if (isNull != other.isNull)
			return false;
		if (numericalValue == null) {
			if (other.numericalValue != null)
				return false;
		} else if (!numericalValue.equals(other.numericalValue))
			return false;
		if (stringValue == null) {
			if (other.stringValue != null)
				return false;
		} else if (!stringValue.equals(other.stringValue))
			return false;
		if (type != other.type)
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}

	@Override
	public int compareTo(Value o) {
		return 0;
	}

	public boolean isBoolean() {
		return (type == ValueType.BOOLEAN);
	}

	public boolean isInteger() {
		return (type == ValueType.INTEGER);
	}

	public boolean isNumerical() {
		return (type == ValueType.NUMERICAL) || (type == ValueType.INTEGER);
	}

	public boolean isString() {
		return (type == ValueType.STRING);
	}

	public boolean getBooleanValue() throws ValueTypeException {
		if (isBoolean()) {
			return booleanValue;
		}
		throw new ValueTypeException("Value is not boolean!");
	}

	public int getIntegerValue() throws ValueTypeException {
		if (!isInteger()) {
			throw new ValueTypeException("Value is not integer!");
		}
		if (bitSize > Integer.SIZE) {
			throw new ValueTypeException(
					"Value's bit size is to large for integer!");
		}
		return (int) integerValue;
	}

	public BigDecimal getNumericalValue() throws ValueTypeException {
		if (!isNumerical()) {
			throw new ValueTypeException("Value is not numerical!");
		}
		return numericalValue;
	}

	public String getStringValue() throws ValueTypeException {
		if (!isString()) {
			throw new ValueTypeException("Value is not a string!");
		}
		return stringValue;
	}

	public Value inclusiveOr(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			return new Value(typeName, getIntegerValue()
					| right.getIntegerValue());
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate inclusive-or-expression '" + toString()
							+ " | " + right.toString() + "'.", e);
		}
	}

	public Value exclusiveOr(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			return new Value(typeName, getIntegerValue()
					^ right.getIntegerValue());
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate exclusive-or-expression '" + toString()
							+ " ^ " + right.toString() + "'.", e);
		}
	}

	public Value and(Value right) throws UniversalSyntaxTreeEvaluationException {
		try {
			return new Value(typeName, getIntegerValue()
					& right.getIntegerValue());
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate and-expression '" + toString() + " & "
							+ right.toString() + "'.", e);
		}
	}

	private Value negate() throws UniversalSyntaxTreeEvaluationException {
		try {
			return new Value(typeName, !getBooleanValue());
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate negation '!" + toString() + "'.", e);
		}
	}

	public Value equals(String resultTypeName, Value other) {
		if (equals(other)) {
			return new Value(resultTypeName, true);
		} else {
			return new Value(resultTypeName, false);
		}
	}

	public Value unequals(String resultTypeName, Value other) {
		try {
			return equals(resultTypeName, other).negate();
		} catch (UniversalSyntaxTreeEvaluationException e) {
			throw new RuntimeException(
					"The equality result should be a boolean and this exception should not be here!");
		}
	}

	public Value greaterThan(String typeName, Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			if (isInteger() && right.isInteger()) {
				return new Value(typeName,
						getIntegerValue() > right.getIntegerValue());
			}
			switch (getNumericalValue().compareTo(right.getNumericalValue())) {
			case -1:
				return new Value(typeName, false);
			case 0:
				return new Value(typeName, false);
			case 1:
				return new Value(typeName, true);
			default:
				throw new RuntimeException(
						"compareTo() returned unexpected result!");
			}
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate greater-than-expression '" + toString()
							+ " > " + right.toString() + "'.", e);
		}
	}

	public Value greaterThanOrEquals(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			if (isInteger() && right.isInteger()) {
				return new Value(typeName,
						getIntegerValue() >= right.getIntegerValue());
			}
			switch (getNumericalValue().compareTo(right.getNumericalValue())) {
			case -1:
				return new Value(typeName, false);
			case 0:
				return new Value(typeName, true);
			case 1:
				return new Value(typeName, true);
			default:
				throw new RuntimeException(
						"compareTo() returned unexpected result!");
			}
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate greater-than-or-equal-expression '"
							+ toString() + " >= " + right.toString() + "'.", e);
		}
	}

	public Value lessThan(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			if (isInteger() && right.isInteger()) {
				return new Value(typeName,
						getIntegerValue() < right.getIntegerValue());
			}
			switch (getNumericalValue().compareTo(right.getNumericalValue())) {
			case -1:
				return new Value(typeName, true);
			case 0:
				return new Value(typeName, false);
			case 1:
				return new Value(typeName, false);
			default:
				throw new RuntimeException(
						"compareTo() returned unexpected result!");
			}
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate less-than-expression '" + toString()
							+ " < " + right.toString() + "'.", e);
		}
	}

	public Value lessThanOrEquals(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			if (isInteger() && right.isInteger()) {
				return new Value(typeName,
						getIntegerValue() <= right.getIntegerValue());
			}
			switch (getNumericalValue().compareTo(right.getNumericalValue())) {
			case -1:
				return new Value(typeName, true);
			case 0:
				return new Value(typeName, true);
			case 1:
				return new Value(typeName, false);
			default:
				throw new RuntimeException(
						"compareTo() returned unexpected result!");
			}
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate less-than-or-equals-expression '"
							+ toString() + " < " + right.toString() + "'.", e);
		}
	}

	public Value shiftLeft(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			return new Value(typeName,
					getIntegerValue() << right.getIntegerValue());
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate shift-left-expression '" + toString()
							+ " << " + right.toString() + "'.", e);
		}
	}

	public Value shiftRight(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			return new Value(typeName,
					getIntegerValue() >> right.getIntegerValue());
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate shift-left-expression '" + toString()
							+ " >> " + right.toString() + "'.", e);
		}
	}

	public Value add(Value right) throws UniversalSyntaxTreeEvaluationException {
		try {
			if (isInteger() && right.isInteger()) {
				return new Value(typeName, getIntegerValue()
						+ right.getIntegerValue());
			}
			return new Value(typeName, bitSize, getNumericalValue().add(
					right.getNumericalValue()));
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate add-expression '" + toString() + " + "
							+ right.toString() + "'.", e);
		}
	}

	public Value subtract(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			if (isInteger() && right.isInteger()) {
				return new Value(typeName, getIntegerValue()
						- right.getIntegerValue());
			}
			return new Value(typeName, bitSize, getNumericalValue().subtract(
					right.getNumericalValue()));
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate subtract-expression '" + toString()
							+ " - " + right.toString() + "'.", e);
		}
	}

	public Value multiply(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			if (isInteger() && right.isInteger()) {
				return new Value(typeName, getIntegerValue()
						* right.getIntegerValue());
			}
			return new Value(typeName, bitSize, getNumericalValue().multiply(
					right.getNumericalValue()));
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate multiply-expression '" + toString()
							+ " * " + right.toString() + "'.", e);
		}
	}

	public Value divide(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			if (isInteger() && right.isInteger()) {
				return new Value(typeName, getIntegerValue()
						/ right.getIntegerValue());
			}
			return new Value(typeName, bitSize, getNumericalValue().divide(
					right.getNumericalValue()));
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate divide-expression '" + toString() + " / "
							+ right.toString() + "'.", e);
		}
	}

	public Value remainder(Value right)
			throws UniversalSyntaxTreeEvaluationException {
		try {
			if (isInteger() && right.isInteger()) {
				return new Value(typeName, getIntegerValue()
						% right.getIntegerValue());
			}
			return new Value(typeName, bitSize, getNumericalValue()
					.divideAndRemainder(right.getNumericalValue())[1]);
		} catch (ValueTypeException e) {
			throw new UniversalSyntaxTreeEvaluationException(
					"Cannot evaluate remainder-expression '" + toString()
							+ " % " + right.toString() + "'.", e);
		}
	}
}
