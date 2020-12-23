package com.may.phyo;

import com.may.phyo.entity.Student;

import javax.persistence.*;
import java.util.List;

public class RunnerMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpql_test");
        EntityManager em = emf.createEntityManager();
        Student stu1 = new Student("Shane","java",450000);
        Student stu2 = new Student("Cedrick","PHP",350000);
        Student stu3 = new Student("Kian","Angular",350000);
        Student stu4 = new Student("May Phyo","Spring",200000);
        Student stu5 = new Student("Zune","Spring Boost",150000);
        em.getTransaction().begin();
            em.persist(stu1);
            em.persist(stu2);
            em.persist(stu3);
            em.persist(stu4);
            em.persist(stu5);
        em.getTransaction().commit();

        System.out.println("Select All Student");
        TypedQuery<Student> query = em.createQuery("select s from Student s",Student.class);
        query.getResultList().forEach(System.out::println);

        String queryString = "select s from Student s where s.name ='May Phyo'";
           query = em.createQuery(queryString,Student.class);
           query.getResultStream().forEach(System.out::println);
           List<Student> students = query.getResultList();
           displayQueryResult(queryString,students);

        queryString = "select s from Student s where s.name ='May Phyo' or s.course_fee > 100000";
           query = em.createQuery(queryString,Student.class);
           students = query.getResultList();
           displayQueryResult(queryString,students);

        queryString = "select s from Student s where s.name like 'C%'";
        query = em.createQuery(queryString,Student.class);
        students = query.getResultList();
        displayQueryResult(queryString,students);

        queryString = "select s from Student s where s.name in ('May Phyo','Cedrick')";
        query = em.createQuery(queryString,Student.class);
        students = query.getResultList();
        displayQueryResult(queryString,students);

        queryString = "select s from Student s where s.name not in ('May Phyo','Cedrick')";
        query = em.createQuery(queryString,Student.class);
        students = query.getResultList();
        displayQueryResult(queryString,students);

        queryString = "select s from Student s";
        query = em.createQuery(queryString,Student.class);
        students = query.setFirstResult(2).setMaxResults(2).getResultList();
        displayQueryResult(queryString,students);

        queryString = "select s from Student s where s.name = ?1 or s.course_fee > ?2";
        query= em.createQuery(queryString,Student.class);
        students = query.setParameter(1,"May Phyo").setParameter(2,300000).getResultList();
        displayQueryResult(queryString,students);

        queryString = "select s from Student s where s.name = :name or s.course_fee > :course_fee";
        query = em.createQuery(queryString,Student.class);
        students = query.setParameter("name","Cedrick").setParameter("course_fee",200000).getResultList();
        displayQueryResult(queryString,students);

        queryString = "select s from Student s group by s.course_fee";
        Query myQuery = em.createQuery(queryString);
        List stuList = myQuery.getResultList();
        displayQueryResult2(queryString,stuList);


        queryString = "select s.course_fee,count (s) from Student s group by s.course_fee having s.course_fee>250000";
        myQuery = em.createQuery(queryString);
        stuList = myQuery.getResultList();
        displayQueryResult2(queryString,stuList);



        em.close();
        emf.close();


        com.may.phyo.util.JPAUtil.checkData("select * from student");


    }
    private static void displayQueryResult(String jpql , List<Student> students){
        System.out.println("\n Query Result of \n"+""+jpql +"\" ");
        for (Student stu : students){
            System.out.println("Student : "+stu.getId()+","+stu.getName()+","+stu.getSubject()
                    +","+stu.getCourse_fee());
        }
    }
    private static void displayQueryResult2(String jpql,List stu){
        System.out.println("\n-------- Query result of \"" + jpql + "\"");
        for(Object s:stu){
            Object[] x =(Object[])s;
            System.out.println("Course_Fee => "+ x[0] + ":"+ x[1] + "  people have this course_fee.");
        }
    }

}
