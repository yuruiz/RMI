def getCommonStr(first, second):
        minlen = min([len(first), len(second)])
        if minlen == 0:
            return ""
        for x in xrange(minlen):
            if first[x] != second[x]:
                return first[:x]
        return first[:minlen]
def longestCommonPrefix(strs):

    len_strs = len(strs)

    if len_strs == 0:
        return ""
    if len_strs == 1:
        return strs[0]

    comstr = strs[0]

    for x in xrange(1, len_strs):
        comstr = getCommonStr(comstr, strs[x])
    return comstr

print longestCommonPrefix(["a","a","b"])