package smersh.project.overloader.operator;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.Operation;
import org.springframework.expression.OperatorOverloader;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class ListOperatorOverloader implements OperatorOverloader {

    @Override
    public boolean overridesOperation(Operation operation, Object leftOperand, Object rightOperand) throws EvaluationException {
        return leftOperand instanceof List && rightOperand instanceof List;
    }

    @Override
    public Object operate(Operation operation, Object leftOperand, Object rightOperand) throws EvaluationException {
        var leftLis = (List) leftOperand;
        var rightList = (List) rightOperand;

        if (operation == Operation.ADD) {
            List result = new ArrayList(leftLis);
            result.addAll(rightList);
            return result;
        }
        if (operation == Operation.SUBTRACT) {
            List result = new ArrayList(leftLis);
            result.removeAll(rightList);
            return result;
        }
        if (operation == Operation.DIVIDE) {
            //При Set-е достигается O(1)
            Set setLeft = new HashSet<>(Arrays.asList(leftLis));
            return rightList.stream().filter(O -> !setLeft.contains(O)).toList();
        }
        if (operation == Operation.MULTIPLY) {
            Set setLeft = new HashSet<>(Arrays.asList(leftLis));
            return rightList.stream().filter(setLeft::contains).toList();
        }
        if(operation == Operation.MODULUS || operation == Operation.POWER) {
            checkListTypeNumber(leftLis, rightList);
            List<Number> result = new ArrayList<>();
            for (int i = 0; i < leftLis.size(); i++) {
                if(i < rightList.size()) {
                    result.add(getResultOperation((Number) leftLis.get(i), (Number) rightList.get(i), operation));
                } else {
                    result.add((Number) leftLis.get(i));
                }
            }
            return result;
        }
        return null;
    }

    private void checkListTypeNumber(List<Object> leftList, List<Object> rightList) {
        if(!leftList.isEmpty()) {
            var leftFirst = leftList.getFirst();
            if(!(leftFirst instanceof Number)) {
                throw new ArithmeticException("Данная операция не применима для типов отличных от числовых");
            }
            checkSupportsDivision((Number) leftFirst);
        }
        if(!rightList.isEmpty()) {
            var rightFirst = rightList.getFirst();
            if(!(rightFirst instanceof Number)) {
                throw new ArithmeticException("Данная операция не применима для типов отличных от числовых");
            }
            checkSupportsDivision((Number) rightFirst);
        }
    }

    private void checkSupportsDivision(Number number) {
        var isSupportsDivision = number instanceof Integer ||
                number instanceof Long ||
                number instanceof Float ||
                number instanceof Double ||
                number instanceof BigDecimal ||
                number instanceof BigInteger;
        if (!isSupportsDivision) {
            throw new ArithmeticException("Числовой тип не поддерживает деление");
        }
    }

    public static double getResultOperation(Number left, Number right, Operation operation) {
        if (right.doubleValue() == 0) {
            throw new ArithmeticException("Деление на ноль невозможно");
        }

        // Определение типов чисел и приведение к Long, Double или BigDecimal
        long leftNum = 0;
        long rightNum = 0;

        if (left instanceof Integer || left instanceof Long) {
            leftNum = left.longValue();
        } else if (left instanceof Float || left instanceof Double) {
            leftNum = Math.round(left.doubleValue());
        } else if (left instanceof BigDecimal || left instanceof BigInteger) {
            leftNum = left.longValue();
        } else {
            throw new IllegalArgumentException("Невозможно получить целое число из объекта");
        }

        if (right instanceof Integer || right instanceof Long) {
            rightNum = right.longValue();
        } else if (right instanceof Float || right instanceof Double) {
            rightNum = Math.round(right.doubleValue());
        } else if (right instanceof BigDecimal || right instanceof BigInteger) {
            rightNum = right.longValue();
        } else {
            throw new IllegalArgumentException("Невозможно получить целое число из объекта");
        }

        if(Objects.equals(operation, Operation.MODULUS)) {
            return leftNum % rightNum;
        }
        if(Objects.equals(operation, Operation.POWER)) {
            return Math.pow(leftNum, rightNum);
        }

        throw new IllegalArgumentException("Данная операция не поддерживается");
    }
}
