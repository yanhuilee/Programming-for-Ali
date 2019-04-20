values() valueOf() ordinal() //返回声明中的位置

```java
enum Operation {
  ADD {
  	@Override
  	public int eval(int a, int b) {return a + b;}
  },
  SUBTRACT {
  	@Override
  	public int eval(int a, int b) {return a - b;}
  },
  MULTIPLY {
  	@Override
  	public int eval(int a, int b) {return a * b;}
  },
  DIVIDE {
  	@Override
  	public int eval(int a, int b) {return a / b;}
  };

  public abstract int eval(int a, int b);
  }
Operation op = Operation.SUBTRACT;
int result = op.eval(2, 3);
```