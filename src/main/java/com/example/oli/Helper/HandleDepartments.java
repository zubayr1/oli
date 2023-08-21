package com.example.oli.Helper;

import java.util.List;
import java.util.ArrayList;

public class HandleDepartments
{

    private List<String> departments;
    private List<List<String>> hierarchy_departments;
    public HandleDepartments(List<String> departments, List<List<String>> hierarchy_departments)
    {
        this.departments = departments;
        this.hierarchy_departments = hierarchy_departments;
    }


    public List<List<String>> getHierarchy_departments() {
        return hierarchy_departments;
    }


    public List<Object> doBusiness(String department)
    {
        int count = 0;
        for (String dept : departments)
        {
            if(dept.equals(department))
            {
                count+=1;
            }
        }
        int percentage = (count*100)/ (departments.size());

        DepartmentHierarchy departmentHierarchy = new DepartmentHierarchy(department, getHierarchy_departments());

        List<Object> result = new ArrayList<>();
        result.add(percentage);
        result.add(departmentHierarchy.createHierarchy());

        return result;

    }
    
}
