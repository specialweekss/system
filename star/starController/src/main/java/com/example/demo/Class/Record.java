package com.example.demo.Class;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
@Data
public class Record {
    @TableId
    int userId;
    int paperId;
    int score;


}
