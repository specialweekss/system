package com.example.demo.Class;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Paper
{
    @TableId
    int paperId;
    String question;
    String answer;
    String backGround;
    String num;

}
