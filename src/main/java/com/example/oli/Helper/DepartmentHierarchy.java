package com.example.oli.Helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DepartmentHierarchy {

    private String department;

    private List<List<String>> hierarchy_departments;

    public DepartmentHierarchy(String department, List<List<String>> hierarchy_departments)
    {
        this.department = department;
        this.hierarchy_departments = hierarchy_departments;
    }

    public boolean containsElement(List<List<String>> hierarchy, String element) {
        for (List<String> departmentList : hierarchy) {
            if (departmentList.contains(element)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkcommonsubstring(String a, String b)
    {
        a = a.replace("department", "");
        b = b.replace("department", "");

        int min_length = 0;

        if (a.length()<b.length())
        {
            min_length = a.length();
        }
        else
        {
            min_length = b.length();
        }
        String common = "";
        for(int i=0; i<min_length-1; i++)
        {
            if (a.substring(i, i+1).equals(b.substring(i, i+1)))
            {
                common+=i;
            }
            else
            {
                break;
            }
        }
        if (common.length()>= (min_length/2))
        {
            return true;
        }
        return false;
    }


    public List<List<String>> createHierarchy()
    {
        if (hierarchy_departments.isEmpty())
        {
            List<String> genesis = new ArrayList<>();
            genesis.add(department);

            hierarchy_departments.add(genesis);

            return hierarchy_departments;
        }
        if (!containsElement(hierarchy_departments, department))
        {
            if(department.equalsIgnoreCase("admin") || department.equalsIgnoreCase("admininstration"))
            {
                List<String> adminlist = new ArrayList<>();
                adminlist.add(department);

                hierarchy_departments.add(0, adminlist);
            }
            else
            {
                for (List<String> departmentList : hierarchy_departments)
                {
                    Iterator<String> iterator = departmentList.iterator();
                    boolean found = false;
                    while (iterator.hasNext())
                    {
                        String nestedDepartment = iterator.next();
                        System.out.println(nestedDepartment);

                        if (checkcommonsubstring(nestedDepartment.toLowerCase(), department.toLowerCase())) {
                            found = true;
                            break;
                        }
                    }
                    if (found)
                    {   System.out.println("found");
                        departmentList.add(department);
                        departmentList.sort(Comparator.reverseOrder());

                        return hierarchy_departments;
                    }
                }

                List<String> newlist = new ArrayList<>();
                newlist.add(department);

                hierarchy_departments.add( newlist);

            }
        }
        return hierarchy_departments;

    }
}
