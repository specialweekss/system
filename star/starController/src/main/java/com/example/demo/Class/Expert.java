package com.example.demo.Class;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Expert
{
    @TableId
    int expertId;
    String name;
    String introduction;


}