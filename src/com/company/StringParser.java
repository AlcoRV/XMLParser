package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringParser {
    private String file, mask;

    private Boolean isCorrect(String[] args){
        if(args.length % 2 != 0 || !args[0].equals(XConstant.KEY_INPUT_FILE)){
            return false;
        }

        boolean flag = false;
        for(String arg : args){
            if(arg.equals(XConstant.KEY_INPUT_FILE) || arg.equals(XConstant.KEY_MACK)){
                if(flag) {
                    return false;
                }
            }
            else{
                if(!flag){
                    return false;
                }
            }
            flag = !flag;
        }
        return true;
    }

    public void Parse(String[] args){
        if(isCorrect(args)) {
            file = args[1];
            if (args.length > 2) {
                mask = args[3];
            }
        }
    }

    public String GetFile(){
        return file;
    }

    public ArrayList<String> FilterWithMask(ArrayList<String> list){
        List<String> items = new ArrayList<>(list);
        if(mask == null){
            return new ArrayList<>(items);
        }
        if(!mask.contains(XConstant.ASTERISK)){
            items = items.stream().filter(item -> item.contains(mask)).collect(Collectors.toList());
            return new ArrayList<>(items);
        }
        return Filter(items);
    }

    private ArrayList<String> Filter(List<String> list){
        List<String> newList = list;
        Integer index = mask.indexOf(XConstant.ASTERISK);
        Integer lastIndex = mask.lastIndexOf(XConstant.ASTERISK);
        if(!index.equals(lastIndex)){
            newList = newList.stream().filter(it -> it.contains(mask.substring(index+1, lastIndex))).collect(Collectors.toList());
        }
        if(index != 0){
            newList = newList.stream().filter(it -> it.contains(mask.substring(0, index))).collect(Collectors.toList());
        }
        if(!lastIndex.equals(mask.length()-1)){
            newList = newList.stream().filter(it -> it.endsWith(mask.substring(lastIndex+1))).collect(Collectors.toList());
        }

        return new ArrayList<>(newList);
    }
}
