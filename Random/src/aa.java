
class A {  
         public String show(D obj){  
                return ("A and D");  
         }   
         public String show(A obj){  
                return ("A and A");  
         }   
         public String fun(){
        	 return "asa";
         }
}   
class B extends A{  
         public String show(B obj){  
                return ("B and B");  
         }  
         public String show(A obj){  
                return ("B and A");  
         }
         public String fun(){
        	 return "asas";
         }   
}  
class C extends B{}   
class D extends B{}  
public class aa {
	public static void main(String[] args){
		A a1 = new A();  
        A a2 = new B();  
        B b = new B();  
        C c = new C();   
        D d = new D();   
        System.out.println(a1.show(b)); //A and A  
        System.out.println(a1.show(c));     
        System.out.println(a1.show(d));       
        System.out.println(a2.show(b));//B and A
        System.out.println(a2.show(c));     
        System.out.println(a2.show(d));     
        System.out.println(b.show(b));      
        System.out.println(b.show(c));      
        System.out.println(b.show(d));
//      System.out.println(a2.fun());
//      ①,②,③调用a1.show()方法，a1 属于A类，A类有两个方法show(D obj)和show(A obj)。
//      ①a1.show(b)，参数b为A类的子类对象，这里为向上转型，相当于A obj=b；
//      所以调用show(A obj)方法，得到A and A结果。②同理，③参数为d,调用show(D obj)，得到A and D。
//      ④，⑤，⑥调用a2.show()方法，A a2 = new B();是向上转型，所以对a2方法的调用，使用A1类的方法show(D obj)和show(A obj)，
//      但此时show(A obj)已经被重写为return ("B and A")， ④a2.show(b)，调用show(A obj)，得到B and A。⑤同理，⑥调用show(D obj)，得到A and D。
//      ⑦，⑧，⑨调用b.show()方法,b为B类，B类的show方法有继承的show(D obj)，自己的两个show(B obj)、show(A obj)。
//      ⑦调用show(B obj)，得到B and B，⑧向上转型，调用show(B obj)，得到B and B⑨show(D obj)，得到A and D
	}
	
}
