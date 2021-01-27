package org.apache.velocity.example;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestVelocity
{
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static void main(String[] args)
    {
        TestVelocity tv = new TestVelocity();
        tv.setSource("Alter table tab_name add column1 data_type1, add column2 date_type2, add column3 date_type3,add column4 date_type4");
        tv.up2u("altertab.vm");
    }

    private List<String> parseInfo()
    {
        List<String> ret = new ArrayList<>();
        Pattern p = Pattern.compile("add\\s+(\\w+\\s+\\w+)");
        Matcher m = p.matcher(source);
        m.group(1);
        System.out.println(m.start(1));
        while (m.find())
        {
            ret.add(m.group(1));
        }
        return ret;
    }
    public void up2u(String templateFile)
    {

        try
        {
            /*
             * setup
             */

            Velocity.init("velocity.properties");

            //
            //*  Make a context object and populate with the data.  This
            //*  references (ex. $list) in the template
            //

            VelocityContext context = new VelocityContext();
            context.put("colInfos", parseInfo());

            /*
             *  get the Template object.  This is the parsed version of your
             *  template input file.  Note that getTemplate() can throw
             *   ResourceNotFoundException : if it doesn't find the template
             *   ParseErrorException : if there is something wrong with the VTL
             *   Exception : if something else goes wrong (this is generally
             *        indicative of as serious problem...)
             */

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

            /*
             *  Now have the template engine process your template using the
             *  data placed into the context.  Think of it as a  'merge'
             *  of the template and the data to produce the output stream.
             */

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

            if ( template != null)
                template.merge(context, writer);

            /*
             *  flush and cleanup
             */

            writer.flush();
            writer.close();
        }
        catch( Exception e )
        {
            System.out.println(e);
        }
    }
}
