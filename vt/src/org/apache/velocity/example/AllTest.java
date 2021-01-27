package org.apache.velocity.example;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllTest {


    /**
     * 简单csum函数替换规则，参数不能复杂
     */
    @Test
    public void csumTest(){
        String source = "select csum(sal,hiredate) from emp";
        List<String> ret = new ArrayList<>();
        Pattern p = Pattern.compile("csum\\(([\\w\\d]+),([\\w\\d]+)\\)");
        Matcher m = p.matcher(source);
        while (m.find())
        {
            ret.add(m.group(1));
            ret.add(m.group(2));
        }
        up2u("csum.vm",ret);
    }
    @Test
    public void substringTest(){
        String source = "SELECT SUBSTRING('123456789' FROM 2 FOR 3) FROM EMP";
        List<String> ret = new ArrayList<>();
        Pattern p = Pattern.compile("substring\\(.*from.*for.*\\)");
        Matcher m = p.matcher(source.toLowerCase());
        while (m.find())
        {
            ret.add(m.group());
        }
        String s = ret.get(0).replace("from",",").replace("for",",");
        ret.clear();
        ret.add(s);
        up2u("substring.vm",ret);
    }
    @Test
    public void insertIntoValuesTest(){
        String source = "INSERT INTO tab[(columnList)] values (1, 'col1' , b , 3+a, fun(zx,c))";
        List<String> ret = new ArrayList<>();
        Pattern p = Pattern.compile("values\\s\\((.*)\\)");
        Matcher m = p.matcher(source.toLowerCase());
        while (m.find())
        {
            ret.add(m.group(1));
        }
        up2u("insertIntoValues.vm",ret);
    }
    @Test
    public void trimTest(){
        String source = "INSERT INTO tab select 1, 'col1' , b , 3+a, fun(zx,c) ， trim ( 213) from dual where trim(ad) = 'aa'";
        List<String> ret = new ArrayList<>();
        Pattern p = Pattern.compile("trim\\s*\\(\\s*(\\w*\\d*)\\s*\\)");
        Matcher m = p.matcher(source.toLowerCase());
        while (m.find())
        {
            ret.add("trim (cast ("+ m.group(1) +" as varchar(6000)))");
        }

        up2u("trim.vm",ret);
    }

    /**
     * 未完成
     */
    @Test
    public void keyword1Test(){
                    //   CREATE GLOBAL TEMPORARY TABLE B_DM_BRW_AGT_T AS SELECT * FROM ${MDDM_DB}.$table_target LIMIT (0,0)
        String source = "CREATE VOLATILE MULTISET TABLE B_DM_BRW_AGT_T, NO LOG AS schemaA.$table_target WITH NO DATA ON COMMIT PRESERVE ROWS";
        List<String> ret = new ArrayList<>();
        Pattern p = Pattern.compile("(?i)(?<!create\\sor)\\s?replace");
        Matcher m = p.matcher(source.toLowerCase());
        while (m.find())
        {
            ret.add(m.group(0));
        }

        up2u("replace.vm",ret);
    }

    /**
     * 不需要脚本 正则替换就好
     */
    @Test
    public void replaceTest(){
        String source = "REPALCE VIEW MDDM.SU_CORP_LOAN_ACCT_V AS LOCK VIEW PVIEW_O.SU_CORP_LOAN_ACCT FOR ACCESS SELECT * FROM PVIEW_O.SU_CORP_LOAN_ACCT;";
        List<String> ret = new ArrayList<>();
        Pattern p = Pattern.compile("(?i)(?<!create\\sor)\\s?replace");
        Matcher m = p.matcher(source.toLowerCase());
        while (m.find())
        {
            ret.add(m.group(0));
        }

        up2u("replace.vm",ret);
    }

    @Test
    public void modTest(){
        String source = "select a.b mod c from dual";
        List<String> ret = new ArrayList<>();
        Pattern p = Pattern.compile("(?i)([\\w+\\.\\w+|\\w+]+)\\s+mod\\s+([\\w+\\.\\w+|\\w+])+");
        Matcher m = p.matcher(source.toLowerCase());
        while (m.find())
        {
            ret.add(m.group(0));
        }
        up2u("mod.vm",ret);
    }

    @Test
    public void castTest(){
        String source = "select cast( exp as Date  format 'yyyymmdd') ,cast( exp2 as Date  format 'yyyymmdd') from dual";
        List<String> ret = new ArrayList<>();
        Pattern p = Pattern.compile("(?i)cast\\s*\\(\\s*([\\w+\\.\\w+|\\w+]+)\\s+as\\s+date\\s+format\\s+('yyyy-mm-dd'|'yyyymmdd')\\s*\\)");
        Matcher m = p.matcher(source.toLowerCase());
        while (m.find())
        {
            System.out.println(m.groupCount());
            ret.add(m.group(0));
            ret.add(m.group(1));
            ret.add(m.group(2));
            source= source.replaceFirst(m.group(0),"to_date("+m.group(1)+","+m.group(2)+")");
        }
//        System.out.println(source);
        up2u("cast.vm",ret);
    }

    public void up2u(String templateFile, List<String> regularResult) {

        try
        {
            Velocity.init("velocity.properties");

            VelocityContext context = new VelocityContext();
            context.put("colInfos", regularResult);
            context.put("a", "select cast( exp as Date  format 'yyyymmdd') ,cast( exp2 as Date  format 'yyyymmdd') from dual");
            context.put("aa", "(?i)cast\\s*\\(\\s*([\\w+\\.\\w+|\\w+]+)\\s+as\\s+date\\s+format\\s+('yyyy-mm-dd'|'yyyymmdd')\\s*\\)");

            Template template =  null;

            try
            {
                template = Velocity.getTemplate(templateFile);
            }
            catch( ResourceNotFoundException rnfe )
            {
                System.out.println("Example : error : cannot find template " + templateFile );
            }
            catch( ParseErrorException pee )
            {
                System.out.println("Example : Syntax error in template " + templateFile + ":" + pee );
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

            if ( template != null)
                template.merge(context, writer);

            writer.flush();
            writer.close();
        }
        catch( Exception e )
        {
            System.out.println(e);
        }
    }
//    @Test
//    public void stuTest(){
//
//        ScriptEngineManager manager = new ScriptEngineManager();
//        manager.registerEngineName("velocity", new VelocityScriptEngineFactory());
//        ScriptEngine engine = manager.getEngineByName("velocity");
//
//        System.setProperty(VelocityScriptEngine.VELOCITY_PROPERTIES, "path/to/velocity.properties");
//        String script = "Hello $world";
//        StringWriter writer = new StringWriter();
//        engine.getContext().setWriter(writer);
//        Object result = engine.eval(script);
//        System.out.println(writer);
//
//    }


}
